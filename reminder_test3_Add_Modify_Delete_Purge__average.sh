#!/bin/bash
java -ea -Didea.test.cyclic.buffer.size=1048576 -javaagent:/opt/idea-community-2017.2.2/lib/idea_rt.jar=43427:/opt/idea-community-2017.2.2/bin -Dfile.encoding=UTF-8 -classpath /opt/idea-community-2017.2.2/lib/idea_rt.jar:/opt/idea-community-2017.2.2/plugins/junit/lib/junit-rt.jar:/opt/idea-community-2017.2.2/plugins/junit/lib/junit5-rt.jar:/home/kirill.grushin/IdeaProjects/Reminders/target/test-classes:/home/kirill.grushin/IdeaProjects/Reminders/target/classes:/usr/lib/jvm/icedtea-bin-8/jre/lib/charsets.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/cldrdata.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/dnsns.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/jaccess.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/localedata.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/nashorn.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/sunec.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/sunjce_provider.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/sunpkcs11.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/ext/zipfs.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/jce.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/jsse.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/management-agent.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/resources.jar:/usr/lib/jvm/icedtea-bin-8/jre/lib/rt.jar:/home/kirill.grushin/IdeaProjects/Reminders/ojdbc7.jar:/home/kirill.grushin/.m2/repository/org/apache/httpcomponents/httpclient/4.5.5/httpclient-4.5.5.jar:/home/kirill.grushin/.m2/repository/org/apache/httpcomponents/httpcore/4.4.9/httpcore-4.4.9.jar:/home/kirill.grushin/.m2/repository/commons-logging/commons-logging/1.2/commons-logging-1.2.jar:/home/kirill.grushin/.m2/repository/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:/home/kirill.grushin/.m2/repository/com/googlecode/json-simple/json-simple/1.1.1/json-simple-1.1.1.jar:/home/kirill.grushin/.m2/repository/junit/junit/4.10/junit-4.10.jar:/home/kirill.grushin/.m2/repository/org/hamcrest/hamcrest-core/1.1/hamcrest-core-1.1.jar:/home/kirill.grushin/.m2/repository/org/testng/testng/6.14.3/testng-6.14.3.jar:/home/kirill.grushin/.m2/repository/com/beust/jcommander/1.72/jcommander-1.72.jar:/home/kirill.grushin/.m2/repository/org/apache-extras/beanshell/bsh/2.0b6/bsh-2.0b6.jar:/home/kirill.grushin/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.1.1/junit-jupiter-api-5.1.1.jar:/home/kirill.grushin/.m2/repository/org/apiguardian/apiguardian-api/1.0.0/apiguardian-api-1.0.0.jar:/home/kirill.grushin/.m2/repository/org/opentest4j/opentest4j/1.0.0/opentest4j-1.0.0.jar:/home/kirill.grushin/.m2/repository/org/junit/platform/junit-platform-commons/1.1.1/junit-platform-commons-1.1.1.jar:/opt/idea-community-2017.2.2/plugins/junit/lib/junit-platform-engine-1.0.0-M6.jar:/opt/idea-community-2017.2.2/plugins/junit/lib/junit-vintage-engine-4.12.0-M6.jar:/opt/idea-community-2017.2.2/plugins/junit/lib/junit-platform-launcher-1.0.0-M6.jar:/opt/idea-community-2017.2.2/plugins/junit/lib/junit-jupiter-engine-5.0.0-M6.jar com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit5 tv.zodiac.dev.testAMS_Reminder_Add_Modify_Delete_Purge__average,test3_Add_Modify_Delete_Purge__average
