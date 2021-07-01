'''
Created on Apr 15, 2020

@author: etgre
'''
from PIL import Image
from os import system
import sys
import PySimpleGUI as sg

def showFinish():
        finishWindow = sg.Window('Done', [[sg.Text("Finished!")], [sg.Button('Ok')]])
        while True: 
            event, values = finishWindow.read()
            if event is None or 'Ok':
                break
        finishWindow.close()


class Steganographer(object):
    
    def __init__(self, imagePath, textPath):
        self.imagePath = imagePath
        self.textPath = textPath
    
    
    def encryptText(self):
        image = Image.open(self.imagePath, 'r')
        text = open(self.textPath, "r", errors="ignore").read()
        total = len(text)
        width, height = image.size
        image.load()
        newColor = ""
        binary = ""
        curBits = ""
        colors = []
        red = 0
        green = 0
        blue = 0
        curChar = 0
        done= False
        
        newPath = sg.popup_get_file('Image write path')
        if newPath == None:
            sys.exit(0)    
        
        layout = [[sg.Text("Start Encryption")], [sg.Button("Start")]]
        window = sg.Window("Encryption Panel", layout)
        while True: 
            event, values = window.read()
            if event is None or 'Start':
                break
        window.close()
        for row in range(height-1):
            if done:
                break
            for col in range(width-1):
                colors = list(image.getpixel((col, row)))
                if curChar == len(text):
                    image.save(newPath)
                    done = True
                    break
                if binary == "":
                    binary = '{0:08b}'.format(ord(text[curChar]))
                    curChar += 1 
                newColor = '{0:08b}'.format(colors[0])
                curBits = binary[:2]
                newColor = newColor[:6] + curBits
                red = int(newColor,2)
                binary = binary[2:]
                if curChar == len(text):
                    image.save(newPath)   
                    done = True
                    break
                if binary == "":
                    binary = '{0:08b}'.format(ord(text[curChar]))
                    curChar += 1 
                newColor = '{0:08b}'.format(colors[1])
                curBits = binary[:2]
                newColor = newColor[:6] + curBits
                green = int(newColor,2)
                binary = binary[2:]
                if curChar == len(text):
                    image.save(newPath)
                    done = True
                    break
                if binary == "":
                    binary = '{0:08b}'.format(ord(text[curChar]))
                    curChar += 1 
                newColor = '{0:08b}'.format(colors[2])
                curBits = binary[:2]
                newColor = newColor[:6] + curBits
                blue = int(newColor,2)
                binary = binary[2:]
                image.putpixel((col,row), (red, green, blue, 255))
                system('cls')
                print("%s/%s chars encrypted" % (curChar, total))
        showFinish()
        
         
    def decryptText(self):
        image = Image.open(self.imagePath, 'r')
        width, height = image.size
        image.load()
        newText = ""
        temp = ""
        red = 0
        green = 0
        blue = 0
        rgb = []
        
        layout = [[sg.Text("Start Decryption")], [sg.Button("Start")]]
        window = sg.Window("Decryption Panel", layout)
        while True: 
            event, values = window.read()
            if event is None or 'Start':
                break
        window.close()
        for row in range(height-1):
            for col in range(width-1):
                rgb = list(image.getpixel((col,row)))
                red = '{0:08b}'.format(rgb[0])
                temp += red[6:]
                if len(temp) == 8:
                    newText += chr(int(temp,2))
                    temp = ""
                green = '{0:08b}'.format(rgb[1])
                temp += green[6:]
                if len(temp) == 8:
                    newText += chr(int(temp,2))
                    temp = ""
                blue = '{0:08b}'.format(rgb[2])
                temp += blue[6:]
                if len(temp) == 8:
                    newText += chr(int(temp,2))
                    temp = ""
        writeFile = open(self.textPath, "w", errors="ignore")
        writeFile.write(newText)
        writeFile.close()
        showFinish()

imagePath = sg.popup_get_file('Image to open')
print(imagePath)
if imagePath == None:
    sys.exit(0)
filePath = sg.popup_get_file('File to read/write')
if filePath == None:
    sys.exit(0)
stego = Steganographer(imagePath, filePath)

layout = [  [sg.Text("Choose an option")],
            [sg.Button('Encrypt')], [sg.Button('Decrypt')]  ]
window = sg.Window("Options", layout)

while True:
    event, values = window.read()
    if event is None:
        window.close()
        break
    elif event == 'Encrypt':
        window.close()
        stego.encryptText()
        break
    elif event == 'Decrypt':
        window.close()
        stego.decryptText()
        break
