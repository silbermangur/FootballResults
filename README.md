דוח פרויקט סוף אנדרואיד
אפלקציית תוצאות כדורגל

תיאור הפרויקט:
הפרויקט שבחרנו הוא אפליקציה לניהול תוצאות משחקי כדורגל
לכל קבוצה המתועדת באפליקציה יש אפשרות לראות את מספר הנקודות שלה . סה"כ שערים שהופגעו סה"כ שערים שקיבלו וסה"כ נצחנות , תקויים והפסדים.
 בעת שימוש האפליקציה המשתמש יכול לרשום משחק חדש וגם לעדכן משחק ישן. 
בעת הרשמת משחק חדש יש למלא את השדות להלן:  תאריך , עיר , קבוצת בית וחוץ , גולים לקבוצת בית וגולים לקבוצת חוץ.
למשתמש תהיה האופציה לעבור בין המסכים כאשר במסך הבית יש אפשרות לשני מסכים שונים הראשון הוספה או עדכון משחק ישן ואילו השני הוא צפייה במצב הסטטיסטיקה של הקבוצות
ארכיטקטורה מוצעת:
שחקני מערכת:
Database   
 משמש כמעין מדיום בין מסד הנתונים לתוכנית. נועד לצורך משיכה נוחה של נתונים. 
  Teamstats
 אובייקט המאגד בתוכו , עבור כל קבוצה ,  את כל נתוני המשחקים כלומר כמות הניצחונות , ההפסדים, התקוים, הנקודות , כמות המשחקים, כמות הגולים שהקבוצה הבקיעה, וכמות הגולים שהקבוצה ספגה וכו... 
Match 
 אובייקט המאגד בתוכו את כל נתוני המשחק , תעודת זהות , תאריך , עיר , קבוצת בית וקבוצת חוץ , גולים לכל אחת מהן .
     אוביקט היוצר סכמה עבור הנתונים של משחק בדטהבייסMatch contract 
אובייקט היוצר סכמה עבור נתונים של קבוצה בדטהבייס  Goal contract 
ממשקי משתמש:
  Create match 
המשתמש יכול לייצר משחק חדש הכולל תאריך, עיר , קבוצת בית , קבוצת חוץ וגולים עבור שתי הקבוצות 
כאשר המשחק הוזן במערכת המשתמש ילחץ על כפתור השמירה ויציאה .
המשתמש יחזור למסך "כמות המשחקים" והמשחק להלן ישמר בדטהבייס.
Update match
המשתמש יכול לעדכן כל שדה מהשדות המשחק. כאשר משתמש יעדכן את השדות שהוא רוצה יצטרך ללחוץ על כפתור השמירה , ע"י כך יחזור למסך "כמות המשחקים" והמשחק המעודכן ישמר בדטהבייס.
בעת עדכון משחק מתעדכן באופן אוטומטי סטיסטיקות הקבוצות בהתאם לשנויים.

Delete match 
המשתמש יכול למחוק כל משחק ממסך "כמות המשחקים" .
בעת מחיקת משחק מעודכן באופן אוטומטי סטטיסטיקת הקבוצות בהתאם לשנויים.

סקיצת תכנון מסכים 


חלוקת תפקידים:
1.
* ישבנו יחד ע"מ לכתוב את ה דיאגרמת מחלקות להלן המופיע בנספח א
2.
* בניית קובץ המניפסט
*עיצוב כללי ופיתוח המסך הראשוני בעת פתיחת האפלקציה מסך ה "מיין אקטיביטי"
3.
 *יצירת הדאטה בייס והמודלים בהתאם ובדיקה שאכן הדברים ותקינים לפני המודל
* חיבור אפליקציה אל מסד הנתונים "סקיולייט3"
4.
תכנון מבנה האפליקציה ובניית שאר המחלקות, תוך כדי מתן חשיבות על בדיקת נכונות של מסד הנתונים.
5.
העבודה נמצאת בגיט בכתובת הבאה:
https://github.com/silbermangur/FootballResults
נספח א דיאגרמת מחלקות:
