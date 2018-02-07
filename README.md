# csv2mt940
Adapted for CSV format of Fidor - some information like account number or final balance must be entered by the user

This program can convert a CSV file containing transaction data exported from Fidor into the well-known MT940 (SWIFT) data format.

ATTENTION: Use at your own risk. 

I modernized the code, changed dependencies to Apache Commons where possible and transformed the project to a Netbeans/Maven project. Users can configure default values in a file /home/$PROFILE/.CSV2MT940/config.ini (or the respective equivalent on Windows).

## License
MIT
