import socket

# 1. Define target server details
IP_ADDRESS = "127.0.0.1"  # Change to your target IP
PORT = 2053

# 2. Build a raw binary DNS query for "example.com" (A Record)
# Header: Transaction ID (2B), Flags (2B), Questions (2B), Answer RRs (2B), Authority RRs (2B), Additional RRs (2B)
dns_header = b"\xaa\xbb\x01\x00\x00\x01\x00\x00\x00\x00\x00\x00"
# Question: Query Name ("example.com" -> 7example3com0), Type A (2B), Class IN (2B)
dns_question = b"\x07example\x03com\x00\x00\x01\x00\x01"
dns_packet = dns_header + dns_question

# 3. Create UDP Socket and send data
with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as sock:
    sock.settimeout(5.0)  # Stop waiting after 5 seconds

    print(f"Sending DNS query to {IP_ADDRESS}:{PORT}...")
    sock.sendto(dns_packet, (IP_ADDRESS, PORT))

    try:
        # 4. Listen for the UDP response
        response, server = sock.recvfrom(1024)
        print(f"Received response ({len(response)} bytes) from {server}:")
        print(response)
    except socket.timeout:
        print("Request timed out. No response received.")