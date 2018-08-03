package org.jleopard;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-08-03  下午1:07
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public interface AliPayConstant {

    String APP_ID = "2016091800542578";

    String MERCHANT_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCiQcXfvP0JYKDItqac3q9xspXX3sxjDfBan6s7H/8LQcTc18L87ztKadM3+eFVUAe7cvpvFR19O+O5/JTfFdZBotNeeJQS8N0Nr6vdsbfFwFvxPHXKtt9T6wE4lMKb/7hlKpHJqTsa3UoxfP724WwKCnpkvVUOxoYJL1GZkSJ9p+mPCc1IC+cgyRlsqsVJXbcPyVrsdlQqbjOPR+2MhYwyFQUcLTMKEkz8odB0/5oo3W24SZPpyjQDsi3pqhd9WGBBsKiAXZ/sR9kulUGbNz/1glmnSbUhW1/AqqabeUJG2yenxrYwHWNEiroXKophFdEx2+rKKTzAPfGndUBVw9SFAgMBAAECggEAKTZXkhqokqcub4ylCRcurMxVbv0yIL67m+kOTvgziaJs5EMRTbld7+qtMXyQlWC8dgogPfK5CMQbdXQIRBmB0C4RTd+GjMENBJroJmkralkPgVh/+rs0QlZgewSd4Y20Qk3rtbL8RjiKzcMX55EeJfa/W7MTVatrBA42xbCteAiyjrE+sQKxv18D12yDHMSzdseNkgFB32JQo3kuqLS9rzDdIu/EI5d3wZhm7PxUmsovKIEQvvL2+Rsu4QgrZF39Mol6SzVyUYXhT/ewLgkTcogFd73sgry5AHPW/S1E6c41rqomWNl3Mg8Il/p91Pry1f12ua7uhSrflmR79y9lAQKBgQD5WMYTUxGKaiCloPuV02b3xDrwxV3tk8sMD6NprDNrmU+4E2i5RlNNW6Hx4rynSm/+Arg6YH4uirXBfFT9XYGVvWdR2KdG7aEB1bzD5COIPTTdmPT6KcAAsL36XMWJAMmWKOQvk1aJ3oAzJa3PpvGcU2MyXmXimblAjcsY/s8n4QKBgQCmlhwmb92+1LpN3TYFVU00bhzhiCfAPREVYqP1nx9ukdd5VDvseLun680EqMm0190mlikHmK4z2tNE9glG1qTod5KvEwAsqEehxMqWDJC6CGewx3Fcvq913d45n9zwMPOGiETIU8KC+bz5zzS6PjxS3+G/mchU82haGMWF3VExJQKBgC5vnuvIY5LgctYFpfTgGuP3knZ2eyichorneqpaXHUkZxlD3BPtCeR1NTKj5DhPf43RmdedUsJ+KJ0w92778/8b99rhOz3VUU/TK0Vb/lYH+Alwy+au0cgPIkI29r6t+9zQHrbRfKNOYZek47qxP3u6d9XQiK7rq/lerr0n7xJhAoGAVl4ZNixlcRzlgp0f9t/k6Tv1qVBoG5kNlGLR0vSu3Sqr5nGvp8hosoTkhHfmK+aG+Ax+NTwJhUT8qBJPePSsBwKHgFF/k6eji9F1nxngpzLYX6Tnt0f9vERNhN7HM5iNicB4uMwEWteUDIhLNAw0RltgtW/Ll4+jGKMjNWYrRCkCgYEAtJfkwijQUwjL7GbFxBa6MG1T0Br+zERAqxFfKb8U5ELpf1K86uvgUNUCNDX97/Q/6QwJlzmX9LLIiphqruW7HR4mS0cOWSm1eZ+QrpE4WrSwRD13VEnkVw1TgE8cwxKU3G0RNUetxHmlDW5XW+TyOXLwkslxWREzA6O+gTGomRs=";

    String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwna5m2erkDIzDzHqGMda6qMNvIXO7msF8cbLKqCk9QJspXVumZvgFnThwfRo9tkAc7AvlBekFANOirBQ8Q9C0Z+EjKF0/H7Mat0Y5Fg0RiAa4bQhFy1aomhpRYRYZXJ2LbO5Zt8e3Ut9Yj2K/Kk1B2tHiPz0IYwJFzOlM29z+y12IUgVyn+ltNf1ByOnUi7A66x2Lnw4xGwHCaixPPhy1VZVl1h4BEg0D7fR4S6/X7WHvokcwoCHxUekJ300WVVqqOJapND22waDSjrBIkIVFuh8zkosxAf4Vq8GkSS2CVZjH7Cq/3gz/TZX6UIuPWi/6xUcni/Dsq0mdoq3+1MOqwIDAQAB";

    String NOTIFY_URL = "http://localhost:8080/user/pay/notifySuccess";

    String RETURN_URL = "http://localhost:8080/user/pay/success";

    String SING_TYPE = "RSA2";

    String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";

    String CHAR_SET = "UTF-8";

}
