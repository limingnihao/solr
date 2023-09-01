#!/bin/bash
#当前路径
path=$(pwd)
solrPath=$path/solr/packaging/build/solr-10.0.0-SNAPSHOT
collName=resumeVector
confPath=/Volumes/Workspace/zhaopin/_search/solr-s-config/$collName/env/dev
chmod u+x $solrPath/server/scripts/cloud-scripts/zkcli.sh
$solrPath/server/scripts/cloud-scripts/zkcli.sh -zkhost localhost:2181 -cmd upconfig -confdir $confPath -confname $collName
curl -X GET --user zpadmin:zp_solr@Rocks 'http://localhost:8981/solr/admin/collections?action=RELOAD&name='$collName'&wt=json'