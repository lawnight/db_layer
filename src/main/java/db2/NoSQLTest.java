package db2;

public class NoSQLTest {

    public static void main(String[] args) {

        // MongoMgr.getInstance().init();

        long start = System.currentTimeMillis();
        // mongoTest();
        long start2 = System.currentTimeMillis();
//		redisTest();
        long start3 = System.currentTimeMillis();

        System.err.println("mongoTest cost:" + (start2 - start));
        System.err.println("redisTest cost:" + (start3 - start2));

    }

	/*
    private static void redisTest() {
		MapUnitEntity entity = new MapUnitEntity();
		entity.setId(321312321);
		entity.setX(123);
		MapUnitDAO.getInstance().saveRedis(entity);

		entity = new MapUnitEntity();
		entity.setId(32131);
		entity.setX(1232);
		MapUnitDAO.getInstance().saveRedis(entity);

		int x = MapUnitDAO.getInstance().getRedis(321312321).getX();
	}

	private static void mongoTest() {

		MapUnitEntity entity = new MapUnitEntity();
		entity.setId(321312321);
		entity.setX(123);
		MapUnitDAO.getInstance().saveMongo(entity);

		entity = new MapUnitEntity();
		entity.setId(32131);
		entity.setX(1232);

		MapUnitDAO.getInstance().saveMongo(entity);

		int x = MapUnitDAO.getInstance().getMongo(32131).getX();
	}
	*/
}
