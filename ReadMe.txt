This program generates RSA Key Pair for public and private keys and then will use them to encrypt and decrypt a text message.

The user may view the public and private keys by clicking on the corresponding icons. When the user taps the "Encrypt Text" button the encrypted version of the text gets displayed in the textView below the button that reads "Encrypt Text".

To confirm proper encryption and correct decryption of the encrypted text please use the tag "decrypted" without quotes in the Android logcat to see the text. This is done in class "RSAUtil" and method "performEncryption".

After the RSA Key Pair is generated and the App is restarted or paused the "Generate Key Pair" gets disabled.