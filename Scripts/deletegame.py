#!C:/Python27/python.exe

import MySQLdb
import json
import cgi
#get the url fields
fs = cgi.FieldStorage()

print "Content-Type: text/html; charset=utf-8\n\n";
#connect to the database
db = MySQLdb.connect(host = "localhost",
					 user = "cathal",
					 passwd = "password",
					 db = "tictactoe_db")
cur = db.cursor() 
#delete the game with that id
cur.execute("""DELETE FROM games where id = %d""" %(fs["id"].value)

db.commit()
cur.close()