package db2.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * 读取Spring配置
 * 
 * @author fusheng Liang
 *
 */
public abstract class Context {

	private static final String CONFIG = "app.xml";
	
	private static ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG);
	
	@SuppressWarnings("unchecked")
	public static synchronized <T> T getBean(String s){
		return (T) context.getBean(s);
	}
}
