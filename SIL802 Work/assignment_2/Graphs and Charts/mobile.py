import numpy as np
import matplotlib.pyplot as plt

#Graph 1

y = [4,1,7]
N = len(y)
x = range(N)
#width = 1/1.5
width = 0.35       # the width of the bars: can also be len(x) sequence
plt.bar(x, y, width, color="blue")

plt.xlabel('Type of Mobile Owned')
plt.ylabel('Number of Farmers')
plt.title('Mobile Owned by farmers')

plt.ylim([0,8])

ind = np.arange(N)    # the x locations for the groups

plt.xticks(ind+width/2., ('No Mobile','Smartphone','No Smartphone') )
    
plt.show()