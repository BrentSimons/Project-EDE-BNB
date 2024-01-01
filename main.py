import requests
import time

# Set the API endpoint
api_url = "http://localhost:7000/test/bnb"

# Set the number of requests and interval
num_requests = 9  # Adjust based on your rate limit

# Make API calls
for i in range(num_requests):
    response = requests.get(api_url)
    
    # Display the result
    print(f"Request {i + 1} - Status Code: {response.status_code} - Text: {response.text}")
    