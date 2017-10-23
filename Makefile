CMD = javac
LIB_DIR = lib/
OUT_DIR = out/
SOURCE_DIR = src/
MAIN = src/oktmr/grafthug/RDFRawParser.java
APP_NAME = grafthug.jar
MANIFEST = Manifest.mf

grafthug : grafthug-class
	jar cvfm $(APP_NAME) $(MANIFEST) -C $(OUT_DIR) . $(LIB_DIR)

grafthug-class :
	mkdir -p $(OUT_DIR)
	$(CMD) -extdirs $(LIB_DIR) -sourcepath $(SOURCE_DIR) $(MAIN) -d $(OUT_DIR)

clean:
	rm -f $(APP_NAME)
	rm -rf $(OUT_DIR)
