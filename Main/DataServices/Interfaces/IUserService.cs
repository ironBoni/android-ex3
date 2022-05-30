using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models.DataServices.Interfaces {
    public interface IUserService : IDataService<User, string>, IContactService {
        string GetFullServerUrl(string url);
    }
}
