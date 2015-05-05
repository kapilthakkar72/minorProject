import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [2,7,3]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Area of Land Owned (Acres)')
plt.ylabel('Number of Farmers')
plt.title('Land Owned by Farmers')

plt.ylim([0,8])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('1-5','6-10','20+') )
    
plt.show()