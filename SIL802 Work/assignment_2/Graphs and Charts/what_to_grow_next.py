import matplotlib.pyplot as plt


# The slices will be ordered and plotted counter-clockwise.
labels =  'Depending on price','ancestral crop','Crop rotation (good for land)' 
sizes = [1,10,1]
#colors = ['yellowgreen', 'gold', 'lightskyblue', 'lightcoral']
#explode = (0, 0.1, 0, 0) # only "explode" the 2nd slice (i.e. 'Hogs')

colors = ['yellowgreen', 'lightskyblue' , 'gold']

plt.pie(sizes, labels=labels, colors=colors,
        autopct='%1.1f%%', shadow=True, startangle=90)
# Set aspect ratio to be equal so that pie is drawn as a circle.
plt.axis('equal')
plt.title("How do you decide what to grow next?")
plt.show()