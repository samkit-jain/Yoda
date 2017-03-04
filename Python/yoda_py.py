import os
import socket
import sys

HOST = ''
PORT = 8080

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

try:
	s.bind((HOST, PORT))
except socket.error, msg:
	print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
	sys.exit()

s.listen(10)

while True:
    conn, addr = s.accept()
    print "Connected by: ", addr
    data = conn.recv(1024) #how many bytes of data will the server receive
    print "Received: ", data
    
    if data[0:4] == "open":
    	command = "gnome-terminal -e '" + data[5:] + "'"
    	os.system(command)
    
    conn.close()

s.close()