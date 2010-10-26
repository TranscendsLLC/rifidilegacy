#!/usr/bin/env python

import base_parser

class Bundle:
    def __init__(self):
        self.path_info = None
                
        self.ipackages = []
        self.epackages = []
    def add_ipackage(self, i):
        self.ipackages.add(i)
    def add_epackage(self, e):
        self.epackages.add(e)
    
    
class Version:
    def __init__(self):
        self.major = 0
        self.minor = 0
        self.micro = 0
        self.qual = '0'
            
    def set_major(self, major):
        self.major = major
            
    def set_minor(self, minor):
        self.minor = minor
            
    def set_micro(self, micro):
        self.micro = micro
            
    def set_qual(self, qual):
        self.qual = qual
            
    def isLess(self, version):
        if self.major < version.major:
            return False
        elif self.major > version.major:
            return True
        elif self.minor < version.minor:
            return False
        elif self.minor > version.minor:
            return True
        elif self.micro < version.micro:
            return False
        elif self.micro > version.micro:
            return True
        elif self.qual <= version.qual:
            return False
        else:
            return True
            
    def isEqual(self, version):
        if self.major == version.major and self.minor == version.minor and \
           self.micro == version.micro and self.qual == version.qual:
            return True
        else:
            return False
            
class ImportPackage:
    def __init__(self, name):
        self.name = name
        self.b_version = None
        self.e_version = None
        self.b_inclusive = False
        self.e_inclusive = False
            
    def set_version_range(self, bversion, b_inc, eversion, e_inc):
        assert isinstance(bversion, Version) and isinstance(eversion, Version)
        self.b_version = bversion
        self.b_inclusive = b_inc
        self.e_version = eversion
        self.e_inclusive = e_inc
            
    def is_in_range(self, version):
        if not version.isLess(self.b_version) \
            and not (self.b_inclusive and version.isEqual(self.b_version)):
            return False
        elif not self.e_version.isLess(version) \
            and not(self.e_inclusive and self.e_version.isEqual(version)):
            return False
        else:
            return True
        
        
if __name__ == '__main__':
    pass
