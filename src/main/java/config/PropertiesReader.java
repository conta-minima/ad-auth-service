package config;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jboss.logging.Logger;


public class PropertiesReader {
	
	private static final Logger LOGGER = Logger.getLogger( PropertiesReader.class.getName() );
	
	//SINGLETON
	private static PropertiesReader _INSTANCE;
	
	public static PropertiesReader getInstance() throws Exception {
		//if (_INSTANCE == null){
			_INSTANCE = new PropertiesReader();
		//}
		return _INSTANCE;
	}
	
	
	private PropertiesReader() throws Exception {
		this.readProperties();
	}
	
	private Properties properties;
	
	public Properties getProperties(){
		return this.properties;
	}
	
	private void readProperties() throws Exception{
		try {
			//busca o properties (se nao foi passado por parametro, le o diretorio corrente)
			
			String arquivo = System.getProperty("properties.file");
			if (arquivo == null) {
				//diretorio atual
				File f = new File(System.getProperty("java.class.path"));
				File dir = f.getAbsoluteFile().getParentFile();
				String path = dir.toString();
				arquivo = path + File.separator + "ad-auth-service.properties";
			}
			
			LOGGER.info("Arquivo de propriedades: " + arquivo);
			
			File f = new File ( arquivo );
			if (f.exists()){
				properties = new Properties();
				properties.load(new FileInputStream(f));
			}
			else{
				throw new Exception("Arquivo de propriedades não encontrado ["+f.getAbsolutePath()+"]");
			}
		} catch (IOException e) {
			throw new Exception("Erro ao ler arquivo de propriedades: ["+e.getMessage()+"]", e);
		}
	}

}
