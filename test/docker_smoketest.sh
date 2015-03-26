#!/bin/sh

DEFAULT_HOST="localhost"
DEFAULT_BASEPORT="8080"

HOST=${1:-$DEFAULT_HOST}
BASEPORT=${2:-$DEFAULT_BASEPORT}
SE="POIPROXY"

echo "$HOST"
echo "$BASEPORT"

echo "Entering FIC2Lab smoke test sequence. Vendor's validation procedure of $SE engaged. Target host:port: $HOST:$BASEPORT"

echo "Run smoke test for '$SE is up and running'"

REQUEST_URL=/poiproxy/describeServices
#ITEM_RESULT=`curl -s -o /dev/null -w "%{http_code}" http://$HOST:$BASEPORT$REQUEST_URL`
ITEM_RESULT=`curl -s -w "%{http_code}" http://$HOST:$BASEPORT$REQUEST_URL`
if [ "$ITEM_RESULT" -ne "200" ]; then
    echo "Curl command for $REQUEST_URL failed. Can't get list of available services. Validation procedure terminated."
    echo "Debug information: HTTP code $ITEM_RESULT instead of expected 200 from $HOST"
    exit 1;
else
    echo "Curl command for $REQUEST_URL OK. A list of services available in $SE has been retrieved"
fi

echo "Smoke test completed. Vendor component validation procedure succeeded. Over."
