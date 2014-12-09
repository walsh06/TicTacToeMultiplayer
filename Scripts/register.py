#!C:/Python27/python.exe

import MySQLdb
import cgi
import json
#get url fields
fs = cgi.FieldStorage()

print "Content-Type: text/html; charset=utf-8\n\n";

#connect to the database
db = MySQLdb.connect(host = "localhost",
					user = "cathal",
					passwd = "password",
					db = "tictactoe_db")

cur = db.cursor() 


#get the count of people with that username
cur.execute("""SELECT COUNT(id)
			FROM users
			WHERE username = %s""", fs["username"].value)

#if there was one username is taken
#else add the user to the database
if cur.fetchone()[0] > 0:
	print json.dumps({"success": False, "message": "Sorry, Username taken, please try again!"})
else :
	print json.dumps({"success": True, "message": "User Registered, Now try login!"})
	cur.execute("""INSERT INTO users(username, password)
			VALUES(%s, %s)""", (fs["username"].value, fs["password"].value))
	db.commit()
	cur.close()
