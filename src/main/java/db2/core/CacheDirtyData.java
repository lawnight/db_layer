package db2.core;

import db2.config.AppContext;
import db2.entity.BaseEntity;
import db2.entity.BaseEntity.EntityOperation;
import db2.entity.MapUnitEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 管理需要更新到数据库的数据
 *
 * @author near
 */
public class CacheDirtyData {

    private static final CacheDirtyData INSTANCE = new CacheDirtyData();

    private CacheDirtyData() {
    }

    public static CacheDirtyData getInstance() {
        return CacheDirtyData.INSTANCE;
    }

    PlatformTransactionManager txManager = AppContext.getBean("transactionManager");

    SqlSessionTemplate sqlSessionTemplate = AppContext.getBean("sqlSession");

    //需要update的entity
    private Map<String, List<BaseEntity>> entityMap = new ConcurrentHashMap<>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    // 第二优先 需要保存的数据
    private List<String> secondPriorList = new ArrayList<>(Arrays.asList("abcEntity", "edfEntity"));

    // 所有的entity class
    private List<String> allEntityType = new ArrayList<>();

    // 是否开启事务
    private boolean transactiion = false;

    public void init() {

//        Package pack = BaseEntity.class.getPackage();
//        List<Class<?>> classes = ClassFinder.find(pack.getName());
//
//        for (Class<?> clazz : classes) {
//            if (BaseEntity.class.isAssignableFrom(clazz)) {
//                allEntityType.add(clazz.getSimpleName());
//            }
//        }

        allEntityType.add(MapUnitEntity.class.getSimpleName());

    }

    // 添加需要更新的数据
    public void add(BaseEntity entity) {
        lock.writeLock().lock();
        try {
            List<BaseEntity> entities = entityMap.get(entity.getClassName());
            if (entities == null) {
                entities = new ArrayList<>();
                entityMap.put(entity.getClassName(), entities);
            }
            entities.add(entity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void update1() {
        List<String> toSaveList = new ArrayList<>();
        for (String entityType : allEntityType) {
            if (!entityType.equals(secondPriorList)) {
                toSaveList.add(entityType);
            }
        }
        update(toSaveList);
    }

    public void update2() {
        update(secondPriorList);
    }

    private void update(List<String> toSaveList) {

        List<BaseEntity> tempForsave = getNeedEntity(toSaveList);
        if (tempForsave == null || tempForsave.isEmpty())
            return;
        long start = System.currentTimeMillis();
        TransactionStatus status = null;

        if (transactiion) {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = txManager.getTransaction(def);
        }

        System.err.println("sql executeType:" + sqlSessionTemplate.getExecutorType());

        try {

            //jdbc事务，connection.setAutoCommit(false),手动commit
            for (BaseEntity entry : tempForsave) {
                updateOneEntity(entry);
            }

            if (transactiion) {
                txManager.commit(status);
            }
            long end = System.currentTimeMillis();
            System.err.println("update dirty data cost:" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
            // if (transactiion) {
            // txManager.rollback(status);
            // }
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void updateOneEntity(BaseEntity baseEntity) {
        BaseDAO dao = null;
        dao = baseEntity.getDAO();
        switch (baseEntity.getOperation()) {
            case INSERT:
                dao.insert(baseEntity);
                baseEntity.setOperation(EntityOperation.NONE);
                break;
            case UPDATE:
                dao.update(baseEntity);
                baseEntity.setOperation(EntityOperation.NONE);
                break;
            default:
                break;
        }
    }

    private List<BaseEntity> getNeedEntity(List<String> toSaveList) {

        if (entityMap.isEmpty())
            return null;
        List<BaseEntity> tempForsave = new ArrayList<>();
        lock.writeLock().lock();
        try {
            for (Entry<String, List<BaseEntity>> entry : entityMap.entrySet()) {
                if (!toSaveList.contains(entry.getKey()))
                    continue;
                tempForsave.addAll(new ArrayList<BaseEntity>(entry.getValue()));
            }
            // entityList.clear();
        } finally {
            lock.writeLock().unlock();
        }

        return tempForsave;

    }
}
