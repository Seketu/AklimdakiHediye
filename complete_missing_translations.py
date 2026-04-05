#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET

# Arapça dosyasındaki TÜM stringleri al
ar_file = 'app/src/main/res/values-ar/strings.xml'
ar_tree = ET.parse(ar_file)
ar_root = ar_tree.getroot()

# Arapça'daki tüm string isimleri ve değerleri al
ar_strings = {}
for elem in ar_root.findall('string'):
    name = elem.get('name')
    value = elem.text if elem.text else ''
    ar_strings[name] = value

print(f"Arapça'da bulunan string sayısı: {len(ar_strings)}")

# Diğer 5 dile ekle
target_langs = [
    'values-fr',
    'values-el',
    'values-hu',
    'values-it',
    'values-ru'
]

for lang_folder in target_langs:
    file_path = f'app/src/main/res/{lang_folder}/strings.xml'
    tree = ET.parse(file_path)
    root = tree.getroot()

    existing = {elem.get('name') for elem in root.findall('string')}

    # Arapça'da var olan ama bu dilde yok olan stringleri bul
    missing = set(ar_strings.keys()) - existing

    for name in sorted(missing):
        new_elem = ET.Element('string')
        new_elem.set('name', name)
        # Arapça değeri koy
        new_elem.text = ar_strings[name]
        root.append(new_elem)

    tree.write(file_path, encoding='utf-8', xml_declaration=True)
    print(f'✓ {lang_folder}: {len(missing)} string eklendi (Toplam: {len(existing) + len(missing)})')

