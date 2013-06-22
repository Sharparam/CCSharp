import sys
import json

script = sys.argv[0]

if len(sys.argv) < 2:
    print "{0} called with no arguments, expected build number".format(script)
    sys.exit(1)

build = None

try:
    build = int(sys.argv[1])
except ValueError:
    print "{0} called with non-integer argument, expected build number".format(script)
    sys.exit(1)

VERSION_FORMAT = '{0}-{1}-MC{2}' # Version-Build-MCVersion

mod_info = open('CCSharp/src/main/resources/mcmod.info')
mod_data = json.loads(mod_info.read())
mod = mod_data["modlist"][0]
version = mod["version"]
mcversion = mod["mcversion"]
mod_info.close()

print VERSION_FORMAT.format(version, build, mcversion)
