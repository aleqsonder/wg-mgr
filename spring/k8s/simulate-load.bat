@echo off

kubectl run -i --tty load-generator --rm --image=busybox:1.28 --restart=Never -- ^
	/bin/sh -c "while sleep 0.001; do wget -q -O- http://backend/api/ > /dev/null; done"
