# MoneyView
Utility to read bank and credit card statements to extract line items as CSV.

### Purpose

This is a personal project which is working on a couple different money related goals.

First, I needed to have CSV formatted information from my bank and credits cards 
in order to rapidly evaluate the nature of our income and expense.  That work is under 
the 'moneyscan' folder.  It looks for line-like information in PDF files.  It does not 
actually know or understand PDFs at all.  I have had to adjust it a number of times 
when I find it has missed content.  User beware.

Second, I was using some of the free financial services to evaluate stocks.  This 
work is under the 'moneyview' folder.  I was never able to retrieve the kind of metrics 
I was looking for.  Those are simply not available through the free APIs.  But it was fun 
to play with.

To try stuff, look at the 'Main' classes.  These are the entry points.
