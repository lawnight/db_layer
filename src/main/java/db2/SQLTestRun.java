package db2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import db2.config.AppContext;
import db2.core.CacheDirtyData;
import db2.dao.MapUnitDAO;
import db2.entity.BaseEntity.EntityOperation;
import db2.entity.MapUnitEntity;

public class SQLTestRun {

    public static void main(String[] args) {


        List<MapUnitEntity> result = new ArrayList<>();
        int count = 10;
        for (int i = 0; i < count; i++) {
            MapUnitEntity mapUnitEntity = new MapUnitEntity();
            mapUnitEntity.setType(i);
            mapUnitEntity.setX(i);
            mapUnitEntity.setY(i);
            mapUnitEntity.setId(i);
            mapUnitEntity.setInited(true);
            mapUnitEntity.setOperation(EntityOperation.INSERT);
            result.add(mapUnitEntity);
            // AppContext.mapUnitDAO.insert(mapUnitEntity);
        }

        CacheDirtyData.getInstance().init();

        long start11 = System.currentTimeMillis();
        //全部插入
        CacheDirtyData.getInstance().update1();
        long start22 = System.currentTimeMillis();
        System.err.println("update1 const:" + (start22 - start11));

        /*
        MapUnitEntity mapUnitEntity = MapUnitDAO.getInstance().get(10034);
        System.err.println(mapUnitEntity);
        long start1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            mapUnitEntity = MapUnitDAO.getInstance().get(i);
        }
        long start2 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            mapUnitEntity = MapUnitDAO.getInstance().get(i);
        }
        long start3 = System.currentTimeMillis();

        System.err.println("cost:" + (start3 - start2));
        Collection<MapUnitEntity> entities =
                MapUnitDAO.getInstance().listAll();
        int i = 0;

        for (MapUnitEntity mapUnitEntity2 : entities) {
            if (i++ > 2) {
                break;
            }
            mapUnitEntity2.setId((int) (Math.random() * 100000));
        }
        CacheDirtyData.getInstance().update1();
        */
    }

}
