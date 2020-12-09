from time import sleep
import yaml

from google.cloud import pubsub_v1


def callback(message):
    print(f"New line: \n {message.data} \n")
    message.ack()


with open("config.yaml", "r") as f:
    data = yaml.safe_load(f)

subscriber = pubsub_v1.SubscriberClient()
topic_name = f"projects/{data['project']}/topics/{data['topic']}"
subscription_name = f"projects/{data['project']}/subscriptions/subscription-python"
try:
    subscriber.create_subscription(name=subscription_name, topic=topic_name)
except Exception:
    print("Subscription already exists!")

future = subscriber.subscribe(subscription_name, callback)
while True:
    try:
        future.result()
    except KeyboardInterrupt:
        future.cancel()
    sleep(data["sleep"])
