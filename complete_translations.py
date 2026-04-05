#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import xml.etree.ElementTree as ET
import os
from pathlib import Path

# Tüm diller için kapsamlı çeviriler
translations = {
    'values-fr': {  # Français
        'special_day_main_title': 'Rappels enregistrés',
        'name': 'Nom',
        'mother': 'Mère',
        'father': 'Père',
        'friend': 'Ami',
        'teacher': 'Professeur',
        'select_date': 'Sélectionner une date',
        'error_select_relationship': 'Veuillez sélectionner votre relation',
        'error_name_required': 'Le nom ne peut pas être vide',
        'label_gift_reason': 'Pourquoi offrons-nous ce cadeau?',
        'button_create_reminder': 'Créer un rappel',
        'no_gifts_yet': 'Aucun cadeau encore'
    },
    'values-ru': {  # Русский
        'special_day_main_title': 'Сохраненные напоминания',
        'name': 'Имя',
        'mother': 'Мать',
        'father': 'Отец',
        'friend': 'Друг',
        'teacher': 'Учитель',
        'select_date': 'Выберите дату',
        'error_select_relationship': 'Пожалуйста, выберите ваши отношения',
        'error_name_required': 'Имя не может быть пустым',
        'label_gift_reason': 'Почему мы дарим этот подарок?',
        'button_create_reminder': 'Создать напоминание',
        'no_gifts_yet': 'Подарков еще нет'
    },
    'values-ar': {  # العربية
        'special_day_main_title': 'التذكيرات المحفوظة',
        'name': 'الاسم',
        'mother': 'الأم',
        'father': 'الأب',
        'friend': 'صديق',
        'teacher': 'المعلم',
        'select_date': 'تحديد التاريخ',
        'error_select_relationship': 'يرجى تحديد علاقتك',
        'error_name_required': 'لا يمكن أن يكون الاسم فارغًا',
        'label_gift_reason': 'لماذا نعطي هذا الهدية?',
        'button_create_reminder': 'إنشاء تذكير',
        'no_gifts_yet': 'لا توجد هدايا حتى الآن'
    },
    'values-bg': {  # Български
        'special_day_main_title': 'Запазени напоминания',
        'name': 'Име',
        'mother': 'Майка',
        'father': 'Баща',
        'friend': 'Приятел',
        'teacher': 'Учител',
        'select_date': 'Избор на дата',
        'error_select_relationship': 'Моля, изберете вашата връзка',
        'error_name_required': 'Име не може да е празно',
        'label_gift_reason': 'Защо даваме този подарък?',
        'button_create_reminder': 'Създайте напоминание',
        'no_gifts_yet': 'Няма подаръци'
    },
    'values-el': {  # Ελληνικά
        'special_day_main_title': 'Αποθηκευμένες υπενθυμίσεις',
        'name': 'Όνομα',
        'mother': 'Μητέρα',
        'father': 'Πατέρας',
        'friend': 'Φίλος',
        'teacher': 'Δάσκαλος',
        'select_date': 'Επιλέξτε ημερομηνία',
        'error_select_relationship': 'Επιλέξτε τη σχέση σας',
        'error_name_required': 'Το όνομα δεν μπορεί να είναι κενό',
        'label_gift_reason': 'Γιατί δίνουμε αυτό το δώρο;',
        'button_create_reminder': 'Δημιουργία υπενθύμισης',
        'no_gifts_yet': 'Κανένα δώρο ακόμα'
    },
    'values-it': {  # Italiano
        'special_day_main_title': 'Promemoria salvati',
        'name': 'Nome',
        'mother': 'Madre',
        'father': 'Padre',
        'friend': 'Amico',
        'teacher': 'Insegnante',
        'select_date': 'Seleziona data',
        'error_select_relationship': 'Seleziona la tua relazione',
        'error_name_required': 'Il nome non può essere vuoto',
        'label_gift_reason': 'Perché diamo questo regalo?',
        'button_create_reminder': 'Crea promemoria',
        'no_gifts_yet': 'Nessun regalo ancora'
    },
    'values-da': {  # Dansk
        'special_day_main_title': 'Gemte påmindelser',
        'name': 'Navn',
        'mother': 'Moder',
        'father': 'Far',
        'friend': 'Ven',
        'teacher': 'Lærer',
        'select_date': 'Vælg dato',
        'error_select_relationship': 'Vælg dit forhold',
        'error_name_required': 'Navn kan ikke være tomt',
        'label_gift_reason': 'Hvorfor giver vi denne gave?',
        'button_create_reminder': 'Opret påmindelse',
        'no_gifts_yet': 'Ingen gaver endnu'
    },
    'values-hu': {  # Magyar
        'special_day_main_title': 'Mentett emlékeztetők',
        'name': 'Név',
        'mother': 'Anya',
        'father': 'Apa',
        'friend': 'Barát',
        'teacher': 'Tanár',
        'select_date': 'Dátum kiválasztása',
        'error_select_relationship': 'Válassza ki a kapcsolatát',
        'error_name_required': 'A név nem lehet üres',
        'label_gift_reason': 'Miért adjuk ezt az ajándékot?',
        'button_create_reminder': 'Emlékeztetője',
        'no_gifts_yet': 'Nincs még ajándék'
    },
    'values-nl': {  # Nederlands
        'special_day_main_title': 'Opgeslagen herinneringen',
        'name': 'Naam',
        'mother': 'Moeder',
        'father': 'Vader',
        'friend': 'Vriend',
        'teacher': 'Leraar',
        'select_date': 'Datum selecteren',
        'error_select_relationship': 'Selecteer uw relatie',
        'error_name_required': 'Naam kan niet leeg zijn',
        'label_gift_reason': 'Waarom geven we dit cadeau?',
        'button_create_reminder': 'Herinnering maken',
        'no_gifts_yet': 'Nog geen cadeaus'
    }
}

base_path = 'app/src/main/res'
total_added = 0

for lang_folder, lang_strings in translations.items():
    file_path = os.path.join(base_path, lang_folder, 'strings.xml')

    if not os.path.exists(file_path):
        print(f'✗ {lang_folder}: Dosya bulunamadı')
        continue

    try:
        # XML'i oku
        tree = ET.parse(file_path)
        root = tree.getroot()

        # Var olan string isimleri kontrol et
        existing_names = {elem.get('name') for elem in root.findall('string')}

        added = 0
        # Eksik stringleri ekle
        for name, value in lang_strings.items():
            if name not in existing_names:
                new_elem = ET.Element('string')
                new_elem.set('name', name)
                new_elem.text = value
                root.append(new_elem)
                added += 1
                total_added += 1

        # Dosyayı kaydet
        tree.write(file_path, encoding='utf-8', xml_declaration=True)
        print(f'✓ {lang_folder}: {added} yeni string eklendi')

    except Exception as e:
        print(f'✗ {lang_folder}: Hata - {str(e)}')

print(f'\n✓ Toplam {total_added} string tüm dillere eklendi!')

