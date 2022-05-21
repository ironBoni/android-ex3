using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.Models {
    public class UsersList {
        public List<string> FulLList { get; set; }

        public UsersList(List<string> fullList)
        {
            FulLList = fullList;   
        }
    }
}
