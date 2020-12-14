from time import sleep
import json
import random
import yaml

from google.auth import jwt
from google.cloud import pubsub_v1

with open("config.yaml", "r") as f:
    data = yaml.safe_load(f)

# Authentication
if data["credentials"]:
    service_account_info = json.load(open(data["credentials"]))
    audience = "https://pubsub.googleapis.com/google.pubsub.v1.Subscriber"

    credentials = jwt.Credentials.from_service_account_info(
        service_account_info, audience=audience
    )

# Publishing
publisher = pubsub_v1.PublisherClient()
topic_name = f"projects/{data['project']}/topics/{data['topic']}"
while True:
    with open(data['csv_file'], "r") as f:
        line = f.readline()
        while line:
            if bool(random.getrandbits(1)):
                print(f"Published line: \n {line}")
                publisher.publish(topic_name, bytes(line, encoding='utf8'), spam='eggs')
            line = f.readline()
            sleep(data["sleep"])

