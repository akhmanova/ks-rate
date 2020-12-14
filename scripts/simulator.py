from time import sleep
import json
import random
import yaml

from google.auth import jwt
from google.cloud import pubsub_v1

with open("config.yaml", "r") as f:
    data = yaml.safe_load(f)

# Authentication
print("credentials")
if data["credentials"]:
    print("get credentials")
    service_account_info = json.load(open(data["credentials"]))
    audience = "https://pubsub.googleapis.com/google.pubsub.v1.Subscriber"

    credentials = jwt.Credentials.from_service_account_info(
        service_account_info, audience=audience
    )

# Publishing
print("publisher creating")
publisher = pubsub_v1.PublisherClient()
print("pablisher created")
topic_name = f"projects/{data['project']}/topics/{data['topic']}"
try:
    topic = publisher.create_topic(request={"name": topic_name})
    print("Topic created")
except Exception:
    print("Topic exists")
while True:
    print("Opening csv")
    with open(data['csv_file'], "r") as f:
        line = f.readline()
        while line:
            print("cycle")
            if bool(random.getrandbits(1)):
                print(f"Published line: \n {line}")
                publisher.publish(topic_name, bytes(line, encoding='utf8'), spam='eggs')
            line = f.readline()
            print("slipping: ")
            sleep(data["sleep"])

