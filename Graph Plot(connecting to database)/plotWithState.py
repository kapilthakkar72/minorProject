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
	#pprint.pprint(records)

	conn.close()

	return records;

def generateQueryfroWholesale(commodity,year,state):
	return "Select w.date, w.arrival, w.unitofarrival, w.minimumprice, w.maximumprice, w.modalprice, w.unitofprice from wholesaledata as w, mandis as m, commodity as c, variety as v, states as s where c.commodityname = '" + commodity + "' and c.commoditycode = v.commoditycode and v.commqualitycode = w.commqualitycode and s.state ='"+state+"' and m.statecode = s.statecode and m.mandicode = w.mandicode and w.date>='"+ year + "-01-01' and w.date<='" + year + "-12-31'"


def generateQueryForRetail(commodity,year,state):
	return "Select r.date,r.price,r.unitofprice from retailpricedata as r, variety as v, commodity as co, center as c, states as s where s.state ='"+state+"' and s.statecode = c.statecode and c.centercode = r.centercode and co.commodityname = '" + commodity + "' and co.commoditycode = v.commoditycode and v.commqualitycode = r.commqualitycode"
 
def main():
	commodity = raw_input('Enter a commodity: ');
	year = raw_input('Enter Year: ')
	state = raw_input('Enter state: ')
	queryW = generateQueryfroWholesale(commodity,year,state);
	queryR = generateQueryForRetail(commodity,year,state);
	#print query

	# Records is list of list
	recordsW = fetchRecords(queryW)
	recordsR = fetchRecords(queryR)

	#Fetch data from records

	dateList=[]
	minimumpriceList = []
	maximumpriceList = []
	arrivalList = []
	modalpriceList = []


	dateListR = []
	priceR = []

	for record in recordsW:
		dateList.append(record[0])
		minimumpriceList.append(record[3])
		maximumpriceList.append(record[4])
		arrivalList.append(record[1])
		modalpriceList.append(record[5])

	for record in recordsR:
		dateListR.append(record[0])
		priceR.append(record[1])
		print(record)

	# Plot It
	
	# wholesale data
	plt.ylim([0,2200])
	plt.plot(dateList,minimumpriceList)
	plt.plot(dateList,maximumpriceList)
	plt.plot(dateList,modalpriceList)

	# retail data
	plt.plot(dateListR,priceR)

	plt.show()
	return
 
if __name__ == "__main__":
	main()