#!/bin/sh
#stop Server
$(pgrep -f hbase-manager | xargs kill -9 )

echo "Shutdown HBase-Manager....."
#==============================================================================