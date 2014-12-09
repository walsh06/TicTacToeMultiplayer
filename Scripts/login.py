#!C:/Python27/python.exe

import MySQLdb
import json
import cgi
#get url fields
fs = cgi.FieldStorage()

print "Content-Type: text/html; charset=utf-8\n\n";
#connect to the databse
db = MySQLdb.connect(host = "localhost",
					user = "cathal",
					passwd = "password",
					db = "tictactoe_db")

cur = db.cursor() 

#get that user from the database
cur.execute("""SELECT id
			FROM users
			WHERE username = %s AND 
			password = %s""", (fs["username"].value, fs["password"].value))

result = cur.fetchone()

#if that user was in the database return success
#else return failure
if result != None:
	id = result[0]
	print json.dumps({"success": True, "message": "User Logged in", "id": id})
else :
	print json.dumps({"success": False, "message": "User Login Failed"})