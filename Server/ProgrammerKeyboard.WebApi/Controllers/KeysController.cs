using Microsoft.AspNetCore.Mvc;
using ProgrammerKeyboard.WebApi.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Windows.Forms;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace ProgrammerKeyboard.WebApi.Controllers
{

    [Route("api/[controller]")]
    [ApiController]
    public class KeysController : ControllerBase
    {
        public static List<KeysJson> Keys;
        string KeyFile = "keys.json";
        public KeysController() {
            if (Keys == null)
            {
                if (System.IO.File.Exists(KeyFile))
                {
                    Keys = Newtonsoft.Json.JsonConvert.DeserializeObject<List<KeysJson>>
                   (System.IO.File.ReadAllText(KeyFile));
                }
                else
                {
                    Keys = new List<KeysJson>();
                }
            }
          
        }
        [HttpGet]
        public IEnumerable<KeysJson> Get()
        {
            return Keys;
        }

        // GET api/<CSharpKeyboardController>/if
        [HttpGet("{id}")]
        public IEnumerable<KeysJson> Get(string id)
        {
            return Keys.Where(i => i.Language == id);
        }

        [HttpPost]
        public void Add([FromBody] KeysJson keysJson)
        {
            keysJson.ID = Guid.NewGuid().ToString();
            Keys.Add(keysJson);
            SaveKeys();
        }

        [HttpDelete]
        public void Remove([FromBody] string keyid)
        {
            var item = Keys.FirstOrDefault(item=>item.ID == keyid);
            if (item != null)
            {
                Keys.Remove(item);
                SaveKeys();
            }
        }

        void SaveKeys() {
            var json = Newtonsoft.Json.JsonConvert.SerializeObject(Keys,Newtonsoft.Json.Formatting.Indented);

            System.IO.File.WriteAllText(KeyFile, json);
        }
    }
}
