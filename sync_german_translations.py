#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET

# Almanca dosyasından eksikleri al
de_file = 'app/src/main/res/values-de-rDE/strings.xml'
de_tree = ET.parse(de_file)
de_root = de_tree.getroot()

# Almanca'daki tüm string isimleri ve değerleri al
de_strings = {}
for elem in de_root.findall('string'):
    name = elem.get('name')
    value = elem.text if elem.text else ''
    de_strings[name] = value

print(f"Almanca'da bulunan string sayısı: {len(de_strings)}")

# Diğer tüm 7 dile Almanca'daki eksikleri ekle
target_langs = [
    'values-ar',
    'values-bg',
    'values-fr',
    'values-el',
    'values-hu',
    'values-it',
    'values-ru',
    'values-da',
    'values-nl'
]

for lang_folder in target_langs:
    file_path = f'app/src/main/res/{lang_folder}/strings.xml'
    tree = ET.parse(file_path)
    root = tree.getroot()

    existing = {elem.get('name') for elem in root.findall('string')}

    # Almanca'da var olan ama bu dilde yok olan stringleri bul
    missing = set(de_strings.keys()) - existing

    if missing:
        for name in sorted(missing):
            new_elem = ET.Element('string')
            new_elem.set('name', name)
            # Almanca değeri koy (kendi dilinde tercüme edilmesi gerekebilir)
            new_elem.text = de_strings[name]
            root.append(new_elem)

        tree.write(file_path, encoding='utf-8', xml_declaration=True)
        print(f'✓ {lang_folder}: {len(missing)} string eklendi (Toplam: {len(existing) + len(missing)})')
    else:
        print(f'✓ {lang_folder}: Zaten tamam (Toplam: {len(existing)})')

