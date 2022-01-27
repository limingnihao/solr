path=$(pwd)

home=/Volumes/Software/Apache/solr_data_9/n1
solrPath=$path/solr/packaging/build/solr-9.0.0-SNAPSHOT
zkhost=localhost:2181/solr_v9

port=8981
portDebug=5001

mkdir $home
mkdir $home/logs

$solrPath/bin/solr start -c -f \
        -s $home \
        -z $zkhost \
        -p $port \
        -m 1g \
        -a -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=$portDebug \
        -Dsolr.solrxml.location=zookeeper \
        -Dsolr.log.dir=$home/logs

