#!/bin/bash
java -ea -Dfile.encoding=UTF-8 -classpath \
./target/test-classes:\
./target/classes:\
./lib/* \
com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit5 tv.zodiac.dev.testAMS_Reminder_Add_Modify_Delete_Purge__average,test1_Add_Purge__average
#$lib/idea_rt.jar:\
#$lib/junit-rt.jar:\
#$lib/junit5-rt.jar:\
#$lib/charsets.jar:\
#$lib/cldrdata.jar:\
#$lib/dnsns.jar:\
#$lib/jaccess.jar:\
#$lib/localedata.jar:\
#$lib/nashorn.jar:\
#$lib/sunec.jar:\
#$lib/sunjce_provider.jar:\
#$lib/sunpkcs11.jar:\
#$lib/zipfs.jar:\
#$lib/jce.jar:\
#$lib/jsse.jar:\
#$lib/management-agent.jar:\
#$lib/resources.jar:\
#$lib/rt.jar:\
#$lib/ojdbc7.jar:\
#$lib/httpclient-4.5.5.jar:\
#$lib/json-simple-1.1.1.jar:\
#$lib/junit-4.10.jar:\
#$lib/testng-6.14.3.jar:\
#$lib/junit-jupiter-api-5.1.1.jar:\
#$lib/junit-platform-commo\ns-1.1.1.jar:\
#$lib/httpcore-4.4.9.jar:\
#$lib/commons-logging-1.2.jar:\
#$lib/opentest4j-1.0.0.jar:\
#$lib/junit-platform-engine-1.0.0-M6.jar:\
#$lib/junit-vintage-engine-4.12.0-M6.jar:\
#$lib/junit-platform-launcher-1.0.0-M6.jar:\
#$lib/junit-jupiter-engine-5.0.0-M6.jar \
