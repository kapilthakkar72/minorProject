import matplotlib.pyplot as plt


# The slices will be ordered and plotted counter-clockwise.
labels = 'No Vehicle', 'Motorcycle' , 'Other Vehicles'
sizes = [2,10]
#colors = ['yellowgreen', 'gold', 'lightskyblue', 'lightcoral']
#explode = (0, 0.1, 0, 0) # only "explode" the 2nd slice (i.e. 'Hogs')

colors = ['yellowgreen', 'lightskyblue']

plt.pie(sizes, labels=labels, colors=colors,
        autopct='%1.1f%%', shadow=True, startangle=90)
# Set aspect ratio to be equal so that pie is drawn as a circle.
plt.axis('equal')
plt.title("Knowledge About Kisan Seva Kendra")
plt.show()