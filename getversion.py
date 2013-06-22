#!/usr/bin/python
# -*- coding: UTF-8 -*-
# 
# getversion.py
# 
# Copyright © 2013 by Adam Hellberg <adam.hellberg@sharparam.com>
# 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
# 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
# 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.
# 

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

mod_data = json.load(open('CCSharp/src/main/resources/mcmod.info'))
mod = mod_data["modlist"][0]
version = mod["version"]
mcversion = mod["mcversion"]

print VERSION_FORMAT.format(version, build, mcversion)
