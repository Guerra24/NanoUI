# Simple script to run nwm in an Xephyr instance.

set -e

# 1. Run.
#
# We need to specify the full path to Xephyr, as otherwise xinit will not
# interpret it as an argument specifying the X server to launch and will launch
# the default X server instead.
XEPHYR=$(whereis -b Xephyr | cut -f2 -d' ')
$XEPHYR :1 \
        -ac \
        -screen 1280x720 \
	-host-cursor
