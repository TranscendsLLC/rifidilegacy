#!/usr/bin/env python

import os
import re
import time
import unittest
import subprocess

from os.path import join
from telnetlib import Telnet
from subprocess import Popen, PIPE

class RifidiSmokeTest(unittest.TestCase):
    
    def test_install(self):    
        ret = subprocess.call(['test', '-e', '/etc/init.d/rifidi-server'])
        ret1 = subprocess.call(['test', '-d', '/usr/local/sbin/rifidi'])
        ret2 = subprocess.call(['test', '-e', '/usr/local/sbin/rifidi/rifidi-server'])
        assert not ret and not ret1 and not ret2
    
    def test_init_script(self):
        ret = subprocess.call(['/etc/init.d/rifidi-server', 'stop'])
        time.sleep(1.0)
        ret = subprocess.call(['/etc/init.d/rifidi-server', 'stop'])
        time.sleep(1.0)

        p1 = Popen(['ps','ax'], stdout=PIPE)
        p2 = Popen(['grep', 'rifidi'], stdin=p1.stdout, stdout=PIPE)
        output = p2.communicate()[0]
        output = output.split('\n')
        for i in output:
            if i == '':
                continue
            elif not re.search('grep', i) and not re.search('python', i):
                self.fail()
        
        ret = subprocess.call(['sudo', '/etc/init.d/rifidi-server', 'start'])        
        p1 = Popen(['ps','ax'], stdout=PIPE)
        p2 = Popen(['grep', 'rifidi'], stdin=p1.stdout, stdout=PIPE)
        output = p2.communicate()[0]
        output = output.split('\n')
        count = 0
 #       print output
#        time.sleep(20.0)
        for i in output:
            if not re.search('grep', i) and not re.search('python', i) and not i == '':
                count += 1
        self.assertEquals(2,count)

    def test_rifidi_server(self):
        
        ret = subprocess.call(['sudo', '/etc/init.d/rifidi-server', 'stop'])
        ret1 = subprocess.call(['sudo', '/etc/init.d/rifidi-server', 'start'])
        assert not ret and not ret1
        
        self.port = '2020'
        self.host = 'localhost'
        telnet = Telnet(self.host, self.port)
        
        #telnet.write('\r\n')
        expected = '\r\nosgi> '
        actual = telnet.read_eager()
        self.assertEqual(expected, actual)
        
        expected = '\r\nosgi> 0:Rifidi App: Diagnostic:GPIO (STOPPED)\r\n'+\
                    '1:Rifidi App: Diagnostic:Serial (STOPPED)\r\n'+\
                    '2:Rifidi App: Diagnostic:Tags (STARTED)\r\n'+\
                    '3:Rifidi App: Diagnostic:TagGenerator (STARTED)\r\n'+\
                    '4:Rifidi App: AppService:ReadZones (STARTED)\r\n'+\
                    '5:Rifidi App: AppService:SensorStatus (STARTED)\r\n'+\
                    '6:Rifidi App: AppService:UniqueTagInterval (STARTED)\r\n'+\
                    '7:Rifidi App: AppService:StableSet (STARTED)\r\n'+\
                    '8:Rifidi App: AppService:UniqueTagBatchInterval (STARTED)\r\n'+\
                    '9:Rifidi App: Monitoring:ReadZones (STARTED)\r\n'+\
                    '10:Rifidi App: Monitoring:Tags (STARTED)\r\n'+\
                    '11:Rifidi App: Monitoring:SensorStatus (STARTED)\r\n'+\
                    'osgi> '
                        
        telnet.write('apps\r\n')
        time.sleep(2.5)
            
        done = False
        while not done:
            value = ''
            value = telnet.read_eager()
            actual += value
            if value == '':
                done = True
        self.assertEqual(expected, actual)
        ret1 = subprocess.call(['sudo', '/etc/init.d/rifidi-server', 'stop'])
        ret1 = subprocess.call(['sudo', '/etc/init.d/rifidi-server', 'start'])
        print '--->'+actual+'<---'
            
            
if __name__ == '__main__':
    unittest.main() 