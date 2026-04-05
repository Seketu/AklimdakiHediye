#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET

# Almanca'daki tüm stringleri al
de_file = 'app/src/main/res/values-de-rDE/strings.xml'
de_tree = ET.parse(de_file)
de_root = de_tree.getroot()

de_strings = {}
for elem in de_root.findall('string'):
    name = elem.get('name')
    value = elem.text if elem.text else ''
    de_strings[name] = value

print(f"Almanca'da {len(de_strings)} string bulundu\n")

# Fransızca'yı kontrol et
fr_file = 'app/src/main/res/values-fr/strings.xml'
fr_tree = ET.parse(fr_file)
fr_root = fr_tree.getroot()

fr_strings = {}
for elem in fr_root.findall('string'):
    name = elem.get('name')
    value = elem.text if elem.text else ''
    fr_strings[name] = value

print(f"Fransızca'da {len(fr_strings)} string bulundu\n")

# Eksikleri bul
missing_in_fr = set(de_strings.keys()) - set(fr_strings.keys())
print(f"Eksik strings: {len(missing_in_fr)}")
if len(missing_in_fr) > 0 and len(missing_in_fr) <= 20:
    for name in sorted(missing_in_fr):
        print(f"  - {name}")

