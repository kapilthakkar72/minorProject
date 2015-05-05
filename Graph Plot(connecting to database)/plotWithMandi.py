import psycopg2
import sys
import pprint
import matplotlib.pyplot as plt

def fetchRecords(query):
	#Define our connection string
	conn_string = "host='localhost' dbname='Agriculture' user='postgres' password='password'"
 
	# print the connection string we will use to connect
	print "Connecting to database\n	->%s" % (conn_string)
 
	# get a connection, if a connect cannot be made an exception will be raised here
	conn = psycopg2.connect(conn_string)
 
	# conn.cursor will return a cursor object, you can use this cursor to perform queries
	cursor = conn.cursor()
	#print "Connected!\n"

	# execute our Query
	cursor.execute(query)
 
	# retrieve the records from the database
	records = cursor.fetchall()
 
	# print out the records using pretty print
	# note that the NAMES of the columns are not shown, instead just indexes.
	# for most people this isn't very useful so we'll show you how to return
	# columns as a dictionary (hash) in the next example.
	pprint.pprint(records)

	conn.close()

	return records;

def generateQuery(commodity,year,mandi):
	return "Select w.date, w.arrival, w.unitofarrival, w.minimumprice, w.maximumprice, w.modalprice, w.unitofprice from wholesaledata as w, mandis as m, commodity as c, variety as v where c.commodityname = '" + commodity + "' and c.commoditycode = v.commoditycode and v.commqualitycode = w.commqualitycode and m.mandiname ='"+mandi+"' and m.mandicode = w.mandicode and w.date>='"+ year + "-01-01' and w.date<='" + year + "-12-31'"
 
def main():
	commodity = raw_input('Enter a commodity: ');
	year = raw_input('Enter Year: ')
	mandi = raw_input('Enter Mandi: ')
	query = generateQuery(commodity,year,mandi);
	print query

	# Records is list of list
	records = fetchRecords(query)

	#Fetch data from records

	dateList=[]
	minimumpriceList = []
	maximumpriceList = []
	arrivalList = []
	modalpriceList = []

	for record in records:
		dateList.append(record[0])
		minimumpriceList.append(record[3])
		maximumpriceList.append(record[4])
		arrivalList.append(record[1])
		modalpriceList.append(record[5])

	# Plot It
	N = len(records)	

	plt.ylim([100,2200])
	plt.plot(dateList,minimumpriceList)
	plt.plot(dateList,maximumpriceList)
	plt.plot(dateList,modalpriceList)	    
	plt.show()
	return
 
if __name__ == "__main__":
	main()