import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [3,2,3,1,1,1,1]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Vehicle (M-Motorcycle, T-Tractor, C-Car, Te-Tempo)')
plt.ylabel('Number of Farmers')
plt.title('Vehicle Owned by farmers')

plt.ylim([0,5])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('No Vehicle','M', 'M & T','M & T & C','M & C', 'Te', 'T') )
    
plt.show()