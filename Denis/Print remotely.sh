#Uses passwordless ssh via rsa key.
#!/bin/bash
USERNAME=denis
HOSTS="10.10.1.18"
SCRIPT="sudo lp -o test.txt"
for HOSTNAME in ${HOSTS} ; do
    ssh -o StrictHostKeyChecking=no -l ${USERNAME} ${HOSTNAME} -p 1220 "${SCRIPT}"
done