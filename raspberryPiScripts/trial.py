Python 2.7.9 (default, Mar  8 2015, 00:52:26) 
[GCC 4.9.2] on linux2
Type "copyright", "credits" or "license()" for more information.
>>> print "Hello"
Hello
>>> x=2
>>> x
2
>>> x+4
6
>>> curl

Traceback (most recent call last):
  File "<pyshell#4>", line 1, in <module>
    curl
NameError: name 'curl' is not defined
>>> import pycurl

Traceback (most recent call last):
  File "<pyshell#5>", line 1, in <module>
    import pycurl
ImportError: No module named pycurl
>>> import pycurl
>>> c = pycurl.Curl()
>>> c.setopt(c.URL, 'https://$brz4lif:$F0rgetme.cloudant.com)
	 
SyntaxError: EOL while scanning string literal
>>> c.setopt(c.URL, 'https://$brzlif:$F0rgetme.cloudant.com')
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#10>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brzlif')
>>> c.setopt(c.URL, 'https://$brz4lif:$F0rgetme.cloudant.com')
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#12>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif')
>>> c.setopt(c.URL, 'https://$brz4lif:$F0rgetme.cloudant.com')
>>> c.perform
<built-in method perform of pycurl.Curl object at 0xb5709630>
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#15>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif')
>>> c.setopt(c.URL, 'https://brz4lif:F0rgetme.cloudant.com')
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#17>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: brz4lif')
>>> >>> c.setopt(c.URL, "https://$brz4lif:$F0rgetme.cloudant.com")
SyntaxError: invalid syntax
>>>  c.setopt(c.URL, 'https://$brz4lif:$F0rgetme.cloudant.com')
 
  File "<pyshell#19>", line 1
    c.setopt(c.URL, 'https://$brz4lif:$F0rgetme.cloudant.com')
    ^
IndentationError: unexpected indent
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#20>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: brz4lif')
>>> c.setopt(c.URL, 'http://$brz4lif:$F0rgetme.cloudant.com')
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#22>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif')
>>> c.setopt(c.URL, '$brz4lif:$F0rgetme.cloudant.com')
>>> python -c 'import base64; print base64.urlsafe_b64encode("brz4lif:F0rgetme")
SyntaxError: EOL while scanning string literal
>>>  c.setopt(c.WRITEDATA, buffer)
 
  File "<pyshell#25>", line 1
    c.setopt(c.WRITEDATA, buffer)
    ^
IndentationError: unexpected indent
>>> c.setopt(c.WRITEDATA, buffer)

Traceback (most recent call last):
  File "<pyshell#26>", line 1, in <module>
    c.setopt(c.WRITEDATA, buffer)
TypeError: functions are not supported for this option
>>> c.setopt(c.WRITEFUNCTION, buffer)
>>> buffer = StringIO()

Traceback (most recent call last):
  File "<pyshell#28>", line 1, in <module>
    buffer = StringIO()
NameError: name 'StringIO' is not defined
>>> clear

Traceback (most recent call last):
  File "<pyshell#29>", line 1, in <module>
    clear
NameError: name 'clear' is not defined
>>> from StringIO import StringIO
>>> buffer = StringIO()
>>> c = pycurl.Curl()
>>> c.setopt(c.URL, 'http://$brz4lif:$F0rgetme.cloudant.com')
>>> c.setopt(c.WRITEDATA, buffer)
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#35>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif')
>>> c.setopt(c.URL, 'https://$brz4lif:$F0rgetme.cloudant.com')
>>> c.setopt(c.WRITEDATA, buffer)
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#38>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif')
>>> c.setopt(c.URL, 'http://$brz4lif:$F0rgetme@$brz4lif.cloudant.com')
\
>>> c.setopt(c.URL, 'http://$brz4lif:$F0rgetme@$brz4lif.cloudant.com')
>>> c.setopt(c.WRITEDATA, buffer)
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#42>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif.cloudant.com')
>>> c.setopt(c.URL, 'http://$brz4lif:$F0rgetme@brz4lif.cloudant.com')
>>> c.perform()
>>> buffer
<StringIO.StringIO instance at 0xb4e80dc8>
>>> buffer.getvalue()
'{"error":"unauthorized","reason":"Name or password is incorrect"}\n'
>>> c.setopt(c.URL, 'https://$brz4lif:$F0rgetme@$brz4lif.cloudant.com')
>>> c.perform()

Traceback (most recent call last):
  File "<pyshell#48>", line 1, in <module>
    c.perform()
error: (6, 'Could not resolve host: $brz4lif.cloudant.com')
>>> c.setopt(c.URL, 'https://$brz4lif:$F0rgetme@brz4lif.cloudant.com')
>>> c.perform()
>>> buffer.getvalue()
'{"error":"unauthorized","reason":"Name or password is incorrect"}\n{"error":"unauthorized","reason":"Name or password is incorrect"}\n'
>>> c.setopt(c.URL, 'https://brz4lif:F0rgetme@brz4lif.cloudant.com')
>>> c.perform()
>>> buffer.getvalue()
'{"error":"unauthorized","reason":"Name or password is incorrect"}\n{"error":"unauthorized","reason":"Name or password is incorrect"}\n{"couchdb":"Welcome","version":"1.0.2","cloudant_build":"2505"}\n'
>>> buffer.clear()

Traceback (most recent call last):
  File "<pyshell#55>", line 1, in <module>
    buffer.clear()
AttributeError: StringIO instance has no attribute 'clear'
>>> buffer.truncate()
>>> buffer.truncate(0)
>>> buffer.getvalue()
''
>>> c.setopt(c.URL, 'https://brz4lif:F0rgetme@brz4lif.cloudant.com/animaldb/aardvark')
>>> c.perform()
>>> 
>>> c.perform()
>>> buffer.getvalue()
'{"_id":"aardvark","_rev":"4-bcc447f3ed8b0dc61f904f8470d328da","value":{"rev":"3-fe45a3e06244adbe7ba145e74e57aba5"},"key":"aardvark"}\n{"_id":"aardvark","_rev":"4-bcc447f3ed8b0dc61f904f8470d328da","value":{"rev":"3-fe45a3e06244adbe7ba145e74e57aba5"},"key":"aardvark"}\n'
>>> import json
>>> buffer.truncate(0)
>>> c.perform()
>>> buffer.getvalue()
'{"_id":"aardvark","_rev":"4-bcc447f3ed8b0dc61f904f8470d328da","value":{"rev":"3-fe45a3e06244adbe7ba145e74e57aba5"},"key":"aardvark"}\n'
>>> json = buffer
>>> j = json['key']

Traceback (most recent call last):
  File "<pyshell#69>", line 1, in <module>
    j = json['key']
AttributeError: StringIO instance has no attribute '__getitem__'
>>> json
<StringIO.StringIO instance at 0xb4e80dc8>
>>> import json
>>> buffer
<StringIO.StringIO instance at 0xb4e80dc8>
>>> buffer.getvalue()
'{"_id":"aardvark","_rev":"4-bcc447f3ed8b0dc61f904f8470d328da","value":{"rev":"3-fe45a3e06244adbe7ba145e74e57aba5"},"key":"aardvark"}\n'
>>> x = json.loads(buffer.getvalue())
>>> x
{u'_rev': u'4-bcc447f3ed8b0dc61f904f8470d328da', u'_id': u'aardvark', u'key': u'aardvark', u'value': {u'rev': u'3-fe45a3e06244adbe7ba145e74e57aba5'}}
>>> x['key']
u'aardvark'
>>> y = buffer.getvalue()
>>> y
'{"_id":"aardvark","_rev":"4-bcc447f3ed8b0dc61f904f8470d328da","value":{"rev":"3-fe45a3e06244adbe7ba145e74e57aba5"},"key":"aardvark"}\n'
>>> x = y
>>> x = y['key']

Traceback (most recent call last):
  File "<pyshell#80>", line 1, in <module>
    x = y['key']
TypeError: string indices must be integers, not str
>>> buffer.getvalue()
'{"_id":"aardvark","_rev":"4-bcc447f3ed8b0dc61f904f8470d328da","value":{"rev":"3-fe45a3e06244adbe7ba145e74e57aba5"},"key":"aardvark"}\n'
>>> x = buffer.getvalue()
>>> x['key']

Traceback (most recent call last):
  File "<pyshell#83>", line 1, in <module>
    x['key']
TypeError: string indices must be integers, not str
>>> setopt
