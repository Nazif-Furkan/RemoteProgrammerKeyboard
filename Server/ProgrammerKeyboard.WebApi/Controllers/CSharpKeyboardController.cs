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
    public class CSharpKeyboardController : ControllerBase
    {
        [HttpGet]
        public IEnumerable<KeysJson> Get()
        {
            if (KeysController.Keys == null)
            {
                new KeysController();
            }
            return KeysController.Keys;
        }



        // GET api/<CSharpKeyboardController>/if
        [HttpGet("{id}")]
        public void Get(string id)
        {
            var prevClib = TextCopy.ClipboardService.GetText();

            var item = Get().FirstOrDefault(i=>i.ID == id);
            if (item != null)
            {
                TextCopy.ClipboardService.SetText(item.Content);
                SendKeys.SendWait("^{v}");
                TextCopy.ClipboardService.SetText(prevClib);
            }

        }
    }
}
