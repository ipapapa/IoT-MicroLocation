#!/bin/bash

action=${1}

cecBin="/usr/local/bin/cec-client"
#off and input may not work. This depends on the model of tv. This code is very general and on does work on my LG tv. 
on='echo "on 0" | ${cecBin} -s'
off='echo "standby 0" | ${cecBin} -s'
input='echo "as" | ${cecBin} -s'

do_on()
{
eval ${on} > /dev/null 2>&1
}

do_off()
{
eval ${off} > /dev/null 2>&1
}

do_input()
{
eval ${input} > /dev/null 2>&1
}

case ${action} in

        on)
                do_on
                exit 0
                ;;

        off)
                do_off
                exit 0
                ;;

        input)
                do_input
                exit 0
                ;;

        *)
                echo $"Use one of the following to execute function: $0 {on|off|input}"
                exit 1
                ;;

esac
