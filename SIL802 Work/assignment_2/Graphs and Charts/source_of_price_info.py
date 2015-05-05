import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [6,5,1]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Source of price Information')
plt.ylabel('Number of Farmers')
plt.title('Source of price Information (Mandi rates of commodity)')

plt.ylim([0,8])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('calling commission agent','Does not ask anyone, on the spot','Internet') )
    
plt.show()