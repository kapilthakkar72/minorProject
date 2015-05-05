import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [2,2,4,4]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Education (Standard)')
plt.ylabel('Number of Farmers')
plt.title('Education of the farmers interviewed')

plt.ylim([0,5])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('No Education','1-7', '8-10', '11-12') )
    
plt.show()