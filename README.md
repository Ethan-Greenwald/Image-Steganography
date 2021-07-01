# Image-Steganography-Java
**This is a basic encryption tool that can be used to hide messages within images, which can then be sent to other people with no visible message and later decrypted. Once an image has had text encrypted within it, the difference is essentially unnoticable.**

**Instructions:**

Open the program and select an image from the file explorer to be used as the medium.
Choose whether to encrypt text or decrypt (extract) text from the image.

**Encrypting:**

Select a .txt text file containing the message to be encrypted. Text processing shouldn't take long for any small message.
A progress bar will open, click start. Once finished, choose an image write path ending in .jpg or .png (typing the name of a file that does not exist at the designated directory will instead create a new image file).

**Decrypting:**

A progress bar will open, click start. Clicking cancel prior to completion (used in situations such as decoding a larger image, such as a 4k picture, which would otherwise take a fair amount of time) will save the already decrypted text to a designated location.
Choose a .txt text file to write to (typing the name of a file that does not exist at the designated directory will instead create a new image file).

**Program information:**

This program relies on manipulating the least-siginificant-bits for individual pixels' RGB color values in order to store characters. The encryption process works by first translating the text into binary, in which each character can be represented by 1 byte (8 bits). This is then chunked into 4 groups of 2 bits. Parsing through the image, each pixel's RGB color values are decoded into a 3 integer array, with each integer being a binary byte representing red, green, and blue. The last two bits of each color byte (hence, least significant) are replaced with the current text chunk. In this way, four color values can store a single character (4 x 2 = 8), or 1.33 pixels. 

The decryption process works by extracting the 2 least significant bits from each color value and splicing them together. Once 4 chunks have been combined to form a byte, it's decrypted into the corresponding character and appended to the final text string. This happens for each pixel, as there is no way of knowing how long the message is from the recipient side. A cancel button is implemented for this very reason, as if the image is very large, it's unlikely that the message would take up the entire picture.

**Additional files:**

Included is a .txt file containing the entirety of Shakespeare's _Hamlet_ to use with encryption. Any standard image will work for encryption and decryption.
