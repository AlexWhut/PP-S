"""
This Python client is used to connect to the Java server implemented in ServerSocketMethods.Main.
Run the Java server first before running this client.
"""
import socket

HOST = 'localhost'  # The server's hostname or IP address
PORT = 5050         # The port used by the server

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    print('Connected to server!')
    s.close()
    print('Connection closed.')
