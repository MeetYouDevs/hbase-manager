#!/bin/sh
source /etc/profile

#check JAVA_HOME & java
if [ -z "$JAVA_HOME" ] ; then
    echo "Error: JAVA_HOME not found in your environment."
    exit 1
fi

BASE_HOME=$(cd `dirname $0`; cd ..; pwd)

cd $BASE_HOME

#==============================================================================
#set JAVA_OPTS
JAVA_OPTS="-server -Xms1024m -Xmx1024m -Xmn512m -Xss256k"

#performance Options
JAVA_OPTS="$JAVA_OPTS -XX:+AggressiveOpts"
JAVA_OPTS="$JAVA_OPTS -XX:+UseBiasedLocking"
JAVA_OPTS="$JAVA_OPTS -XX:+UseFastAccessorMethods"
JAVA_OPTS="$JAVA_OPTS -XX:+DisableExplicitGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC"
JAVA_OPTS="$JAVA_OPTS -XX:+UseConcMarkSweepGC"
JAVA_OPTS="$JAVA_OPTS -XX:+CMSParallelRemarkEnabled"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSCompactAtFullCollection"
JAVA_OPTS="$JAVA_OPTS -XX:+UseCMSInitiatingOccupancyOnly"
JAVA_OPTS="$JAVA_OPTS -XX:CMSInitiatingOccupancyFraction=75"

#GC Log Options
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCApplicationStoppedTime"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCTimeStamps"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails"
#debug Options
#JAVA_OPTS="$JAVA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=8065,server=y,suspend=n"
#==============================================================================
TEMP_CLASSPATH="$BASE_HOME/conf:$BASE_HOME/lib/*"
#==============================================================================
#startup server
RUN_CMD="$JAVA_HOME/bin/java"
RUN_CMD="$RUN_CMD -classpath $TEMP_CLASSPATH"
RUN_CMD="$RUN_CMD $JAVA_OPTS"
RUN_CMD="$RUN_CMD com.meiyou.hbase.manager.Application >$BASE_HOME/logs/hbase-manager.log 2>&1 &"

echo $RUN_CMD

eval $RUN_CMD

echo "Start HBase-Manager..."
#==============================================================================