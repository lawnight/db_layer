/**
 * created by tool DAOGenerate
 */
package db2.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import db2.core.WeakCache2;
import db2.entity.MapUnitEntity;


/**
 * created by tool
 */
@CacheNamespace(implementation = WeakCache2.class)
public interface MapUnitSqlDAO {

    @Insert("INSERT INTO t_g_map_unit(id,type,x,y) VALUES (#{id},#{type},#{x},#{y});")
    public void insert(MapUnitEntity entity);

    @Delete("delete from t_g_map_unit where id = #{id}")
    public void delete(int id);

    @Update("update t_g_map_unit set id=#{id},type=#{type},x=#{x},y=#{y} where id = #{id}")
    public void update(MapUnitEntity entity);

    @Select("SELECT * FROM t_g_map_unit WHERE id = #{id} ")
    public MapUnitEntity get(int id);

    @Select("SELECT * FROM t_g_map_unit")
    public List<MapUnitEntity> listAll();

    @Select("SELECT * FROM t_g_map_unit")
    public Map<Integer, MapUnitEntity> mapAll();

    @Select("SELECT * FROM t_g_map_unit WHERE userId=#{userId} ")
    public List<MapUnitEntity> getByUserId(int id);
}