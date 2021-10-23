create table user_subscriptions(
    subscriber_id int8 not null references users,
    channel_id int8 not null references users,
    primary key(subscriber_id, channel_id)
)